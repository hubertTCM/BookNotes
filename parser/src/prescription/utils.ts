import herbInfo from "../../src/herbs.json";
import { NumberKeyWordType, UOMKeyWordType, uomKeyWords } from "./type";

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
    default:
      throw new Error(`unkown number: ${keyword}`);
  }
};

export const toNumber = (text: string): number => {
  let number = 0;

  let format1 = true; // 九八
  let temp = "";
  for (let i = 0; i < text.length; ++i) {
    if (text[i] === "半" || text[i] == "十") {
      format1 = false;
    }
    temp = `${temp}${singleItemToNumber(text[i] as NumberKeyWordType)}`;
    number += singleItemToNumber(text[i] as NumberKeyWordType);
  }
  if (format1) {
    return parseInt(temp, 10);
  }
  return number;
};
