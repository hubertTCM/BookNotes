import { Quantity, PrescriptionItem, NumberKeyWordType, numberKeyWords, UOMKeyWordType, QuantityToken } from "./type";
import { findHerb, findUom, toNumber, toQuanity } from "./utils";

/*
临证指南医案格式：
- 熟地（四两） 牛膝（一两半）
- 制首乌（四两，烘） 枸杞子（去蒂，二两） 归身（二两，用独枝者，去梢） 怀牛膝（二两，蒸） 明天麻（二两，面煨） 三角胡麻（二两，打碎，水洗十次，烘） 黄甘菊（三两，水煎汁） 川石斛（四两，水煎汁） 小黑豆皮（四两，煎汁）
- 
 */
export type Token =
  | {
      type: "herb" | "bracketsStart" | "bracketsEnd" | "data";
      value: string;
    }
  | QuantityToken;

export const parseTokens = (text: string) => {
  if (!text) {
    return null;
  }
  let tokens: Token[] = [];

  let currentTokenValue = "";
  let currentTokenType: Token["type"] | undefined = undefined;
  const endNumber = () => {
    if (currentTokenType == "number") {
      tokens.push({ type: "number", value: currentTokenValue });
      currentTokenValue = "";
      currentTokenType = undefined;
    }
  };
  const endData = () => {
    if (currentTokenType === "data") {
      tokens.push({ type: "data", value: currentTokenValue });
      currentTokenValue = "";
      currentTokenType = undefined;
    }
  };

  let i = 0;
  while (i < text.length) {
    const char = text.charAt(i);
    if (char === "（") {
      tokens.push({ type: "bracketsStart", value: char });
      currentTokenType = undefined;
      ++i;
      continue;
    }
    if (char === "）") {
      endNumber();
      endData();
      tokens.push({ type: "bracketsEnd", value: char });
      currentTokenType = undefined;
      ++i;
      continue;
    }
    const herbData = findHerb(text, i);
    if (herbData !== null) {
      endData();
      endNumber();
      tokens.push({ type: "herb", value: herbData.herb });
      i += herbData.length;
      currentTokenType = undefined;
      continue;
    }

    const uom = findUom(text, i);
    if (uom !== null) {
      endData();
      endNumber();
      tokens.push({ type: "uom", value: uom });
      currentTokenType = undefined;
      i += uom.length;
      continue;
    }
    if (numberKeyWords.includes(char as NumberKeyWordType)) {
      endData();
      currentTokenValue += char;
      if (currentTokenType !== undefined && currentTokenType !== "number") {
        throw new Error(
          `expect tokenType:number|undefined, actual tokenType:${currentTokenType} char:${char} text:${text}`
        );
      }
      currentTokenType = "number";
      ++i;
      continue;
    }

    if (currentTokenType !== "data") {
      if (char === " " || char == "\u3000") {
        endNumber();
        ++i;
        continue;
      }
    }

    endNumber();
    if (currentTokenType === undefined) {
      currentTokenType = "data";
    }
    if (currentTokenType !== "data") {
      throw new Error(`expect tokenType:data, actual tokenType:${currentTokenType} char:${char} text:${text}`);
    }
    currentTokenValue += char;
    ++i;
    continue;
  }
  return tokens;
};
const toQuantityTokens = (tokens: Token[], start: number, end: number): QuantityToken[] => {
  const quantityTokens: QuantityToken[] = [];
  for (let i = start; i < end; ++i) {
    const temp = tokens[i];
    if (temp.type !== "number" && temp.type !== "uom") {
      throw new Error(`token ${JSON.stringify(temp)} is not quantity`);
    }
    quantityTokens.push(temp);
  }
  return quantityTokens;
};
//TODO: can be done better
const extractDetails = (
  herb: string,
  tokens: Token[],
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem => {
  const endToken = tokens[tokens.length - 1];
  let comment: string | undefined = undefined;
  let quantity: Quantity | undefined;
  if (endToken.type === "uom" || endToken.type === "number") {
    // （洗净，另熬膏，一斤）
    let lastDataIndex = tokens.length - 1;
    for (; lastDataIndex >= 0; lastDataIndex--) {
      if (tokens[lastDataIndex].type === "data") {
        break;
      }
    }
    for (let i = 0; i < lastDataIndex + 1; ++i) {
      comment = `${comment || ""}${tokens[i].value}`;
    }
    const quantityTokens = toQuantityTokens(tokens, lastDataIndex + 1, tokens.length);
    quantity = toQuanity(quantityTokens, convertUOM);
    return { herb, quantity, comment };
  }
  // 三角胡麻（四两，打碎，水洗十次，烘）
  // 牛膝（四两半）
  let firstDataIndex = 0;
  for (; firstDataIndex < tokens.length; ++firstDataIndex) {
    if (tokens[firstDataIndex].type === "data") {
      break;
    }
  }
  for (let i = firstDataIndex; i < tokens.length; ++i) {
    comment = `${comment || ""}${tokens[i].value}`;
  }
  const quantityTokens = toQuantityTokens(tokens, 0, firstDataIndex);
  quantity = toQuanity(quantityTokens, convertUOM);
  return { herb, quantity, comment };
};

export const token2PrescriptionItems = (
  tokens: Token[],
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem[] | null => {
  if (!tokens) {
    return null;
  }
  let items: PrescriptionItem[] = [];
  let herb: string | null = null;
  let i = 0;
  while (i < tokens.length) {
    const token = tokens[i];
    if (token.type === "herb") {
      if (herb) {
        items.push({ herb });
      }
      herb = token.value;
      ++i;
      continue;
    }
    if (token.type === "bracketsStart") {
      if (!herb) {
        throw new Error(`expect herb. tokens:${JSON.stringify(tokens.slice(i))}`);
      }
      let endIndex = i + 1;
      let tempTokens: Token[] = [];
      let endToken: Token | undefined = undefined;
      for (endIndex; endIndex < tokens.length; ++endIndex) {
        endToken = tokens[endIndex];
        if (endToken.type === "bracketsEnd") {
          break;
        }
        tempTokens.push(endToken);
      }
      if (!endToken || endToken.type !== "bracketsEnd") {
        throw new Error(`no ) tokens: ${JSON.stringify(tokens.slice(i))}`);
      }
      const item = extractDetails(herb, tempTokens, convertUOM);
      items.push(item);
      herb = null;
      i = endIndex + 1;
      continue;
    }
    ++i;
  }

  if (herb) {
    items.push({ herb });
  }
  if (items.length) {
    return items;
  }
  return null;
};

export const tryParsePrescription = (
  text: string,
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem[] | null => {
  try {
    const tokens = parseTokens(text);
    return tokens ? token2PrescriptionItems(tokens, convertUOM) : null;
  } catch (e) {
    console.log(text);
    console.log("Error:", e);
    return null;
  }
};
