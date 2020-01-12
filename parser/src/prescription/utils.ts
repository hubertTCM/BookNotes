import herbInfo from "../../src/herbs.json";
import { NumberKeyWordType, UOMKeyWordType, uomKeyWords, QuantityToken, Quantity } from "./type";

export const findHerb = (text: string, start: number = 0): { herb: string; length: number } | null => {
  if (!text || text.length <= start) {
    return null;
  }
  const source = start > 0 ? text.substring(start) : text;
  const herb = herbInfo.allHerbNames.find(x => source.startsWith(x));
  if (!herb) {
    return null;
  }
  // 栝蒌实大者一枚
  if (source.startsWith(`${herb}大者`)) {
    return { herb, length: herb.length + 2 };
  }
  return { herb, length: herb.length };
};

export const findUom = (text: string, start: number): UOMKeyWordType | null => {
  if (!text || text.length <= start) {
    return null;
  }
  const source = start > 0 ? text.substring(start) : text;
  const uom = uomKeyWords.find(x => source.startsWith(x));
  return uom ? uom : null;
};

const singleItemToNumber = (keyword: NumberKeyWordType): number => {
  switch (keyword) {
    case "零":
      return 0;
    case "半":
      return 0.5;
    case "一":
      return 1;
    case "二":
      return 2;
    case "三":
      return 3;
    case "四":
      return 4;
    case "五":
      return 5;
    case "六":
      return 6;
    case "七":
      return 7;
    case "八":
      return 8;
    case "九":
      return 9;
    case "十":
      return 10;
    case "百":
      return 100;
    default:
      throw new Error(`unkown number: ${keyword}`);
  }
};

export const formatNumberText = (from: string): string => {
  let to: string = "";
  for (let i = from.length - 1; i >= 0; i--) {
    const temp = singleItemToNumber(from[i] as NumberKeyWordType);
    const digitLength = String(temp).length;
    if (temp > 9) {
      const zeroFillCount = digitLength - 1 - to.length;
      if (zeroFillCount > 0) {
        to = new Array(zeroFillCount + 1).join("零") + to;
      }
      if (i === 0) {
        to = `一${to}`;
      }
      if (zeroFillCount < 0) {
        throw new Error(`invalid number ${from}`);
      }
    } else {
      to = from[i] + to;
    }
  }
  return to;
};

export const toNumber = (text: string): number => {
  const formattedText = formatNumberText(text);
  let result: string = "";
  for (let i = 0; i < formattedText.length; ++i) {
    const temp = singleItemToNumber(formattedText[i] as NumberKeyWordType);
    result = `${result}${temp}`;
  }
  return parseFloat(result);
};

export const tryConvertToNumber = (text: string): number | undefined => {
  try {
    return toNumber(text);
  } catch (e) {
    return undefined;
  }
};

export const toQuanity = (
  tokens: QuantityToken[],
  convertUOM: (quanity: Quantity) => Quantity
): Quantity | undefined => {
  let currentNumber: number | undefined = undefined;
  let quantity: Quantity | undefined = undefined;
  for (let i = 0; i < tokens.length; ++i) {
    const token = tokens[i];
    switch (token.type) {
      case "number":
        currentNumber = toNumber(token.value);
        break;
      case "uom":
        const temp = convertUOM({
          uom: token.value as UOMKeyWordType,
          value: currentNumber !== undefined ? currentNumber : 1
        });
        if (quantity === undefined) {
          quantity = temp;
        } else {
          if (temp.uom != quantity.uom) {
            throw new Error(`uom not match: ${quantity.uom} not match ${temp.uom}`);
          }
          quantity.value += temp.value;
        }
        currentNumber = undefined;
        break;
      default:
        throw new Error(`unkonw token: ${token.type} ${token.value}`);
    }
  }

  if (quantity && currentNumber === 0.5) {
    return { uom: quantity.uom, value: quantity.value + currentNumber };
  }
  return quantity;
};
