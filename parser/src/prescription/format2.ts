import { Quantity, PrescriptionItem, NumberKeyWordType, numberKeyWords, UOMKeyWordType } from "./type";
import { findHerb, findUom, toNumber } from "./utils";

/*
临证指南医案格式：
- 熟地（四两） 牛膝（一两半）
- 制首乌（四两，烘） 枸杞子（去蒂，二两） 归身（二两，用独枝者，去梢） 怀牛膝（二两，蒸） 明天麻（二两，面煨） 三角胡麻（二两，打碎，水洗十次，烘） 黄甘菊（三两，水煎汁） 川石斛（四两，水煎汁） 小黑豆皮（四两，煎汁）
- 
 */
export type Token = {
  type:
    | "herb"
    | "number"
    | "uom"
    | "bracketsStart"
    | "bracketsEnd"
    | "applyQuantityToPrevious"
    | "sameQuantityForAllHerbs"
    | "data";
  value: string;
};

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
    // if (currentTokenType === "data") {
    //   currentTokenValue += char;
    //   ++i;
    //   continue;
    // }
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
  //console.log(JSON.stringify(tokens));
  return tokens;
};

// TODO:
export const token2PrescriptionItems = (
  tokens: Token[],
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem[] | null => {
  if (!tokens) {
    return null;
  }
  let items: PrescriptionItem[] = [];
  let currentItem: PrescriptionItem | undefined = undefined;
  let currentNumber: number | undefined = undefined;
  let applyQuantityToPrevious: boolean = false;
  for (let i = 0; i < tokens.length; ++i) {
    const token = tokens[i];
    switch (token.type) {
      case "herb":
        if (currentItem) {
          currentNumber = undefined;

          if (applyQuantityToPrevious) {
            if (!currentItem.quantity) {
              throw new Error(`not quantity when applyQuantityToPrevious is true. herb:${currentItem.herb}`);
            }
            for (let j = items.length - 1; j >= 0; --j) {
              const temp = items[j];
              if (temp.quantity !== undefined) {
                break;
              }
              temp.quantity = { ...currentItem.quantity };
            }
          }

          items.push(currentItem);
        }
        currentItem = { herb: token.value };
        applyQuantityToPrevious = false;
        break;
      case "number":
        if (!currentItem) {
          throw new Error(`unkown prescription. ${token.type} ${token.value}`);
        }
        currentNumber = toNumber(token.value);
        break;
      case "uom":
        if (!currentItem) {
          throw new Error(`unkown prescription. ${token.type} ${token.value}`);
        }
        const quanity = convertUOM({
          uom: token.value as UOMKeyWordType,
          value: currentNumber !== undefined ? currentNumber : 1
        });
        if (!currentItem.quantity) {
          currentItem.quantity = quanity;
        } else {
          if (currentItem.quantity.uom != quanity.uom) {
            throw new Error(`uom not match: ${quanity.uom} not match ${currentItem.quantity.uom}`);
          }
          currentItem.quantity.value += quanity.value;
        }
        break;
      case "applyQuantityToPrevious":
        applyQuantityToPrevious = true;
      case "sameQuantityForAllHerbs":
        break;
      default:
        throw new Error(`unkonw token: ${token.type} ${token.value}`);
    }
  }

  if (currentItem) {
    items.push(currentItem);
  }
  return items;
};

export const tryParsePrescription = (
  text: string,
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem[] | null => {
  try {
    const tokens = parseTokens(text);
    return tokens ? token2PrescriptionItems(tokens, convertUOM) : null;
  } catch (e) {
    //console.log("Error:", e);
    return null;
  }
};
