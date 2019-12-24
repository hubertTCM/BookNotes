// 生姜三两（切）　大枣十二枚（擘）生姜一两六铢（切）杏仁十六个（去皮尖）白术　茯苓各三两
// 生姜 大枣
import { numberKeyWords, NumberKeyWordType, uomKeyWords, UOMKeyWordType, PrescriptionItem, Quantity } from "./type";
import { findHerb, toNumber } from "./utils";

export type Token = {
  type: "herb" | "number" | "uom" | "comment" | "applyQuantityToPrevious";
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

  let i = 0;
  while (i < text.length) {
    const herb = findHerb(text, i);
    if (herb !== null) {
      tokens.push({ type: "herb", value: herb });
      i += herb.length;
      continue;
    }
    const char = text.charAt(i);
    if (char === "（") {
      endNumber();
      currentTokenType = "comment";
      ++i;
      continue;
    }
    if (char === "）") {
      if (currentTokenType !== "comment") {
        throw new Error(`unexpected token ${char} currentTokenType:${currentTokenType}`);
      }
      tokens.push({ type: "comment", value: currentTokenValue });
      currentTokenValue = "";
      currentTokenType = undefined;
      ++i;
      continue;
    }
    if (currentTokenType === "comment") {
      currentTokenValue += char;
      ++i;
      continue;
    }
    if (char === " ") {
      endNumber();
      ++i;
      continue;
    }
    if (char === "各") {
      tokens.push({ type: "applyQuantityToPrevious", value: "各" });
      ++i;
      continue;
    }
    if (uomKeyWords.includes(char as UOMKeyWordType)) {
      endNumber();
      tokens.push({ type: "uom", value: char });
      ++i;
      continue;
    }
    if (numberKeyWords.includes(char as NumberKeyWordType)) {
      currentTokenValue += char;
      if (currentTokenType !== undefined && currentTokenType !== "number") {
        throw new Error("failed");
      }
      currentTokenType = "number";
      ++i;
      continue;
    }
    ++i;
  }
  return tokens;
};

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
      case "comment":
        if (!currentItem) {
          throw new Error(`unkown prescription. ${token.type} ${token.value}`);
        }
        if (currentItem.comment) {
          throw new Error(`comment already set. prescrition:${JSON.stringify(currentItem)} comment ${token.value}`);
        }
        currentItem.comment = token.value;
        break;
      case "number":
        if (!currentItem) {
          throw new Error(`unkown prescription. ${token.type} ${token.value}`);
        }
        currentNumber = toNumber(token.value as NumberKeyWordType);
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
    console.log("Error:", e);
    return null;
  }
};
