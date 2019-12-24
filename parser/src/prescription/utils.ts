import herbInfo from "../../src/herbs.json";
import { NumberKeyWordType } from "./type.js";

export const findHerb = (text: string, start: number = 0): string | null => {
  if (!text || text.length <= start) {
    return null;
  }
  const source = start > 0 ? text.substring(start) : text;
  const herb = herbInfo.allHerbNames.find(x => source.startsWith(x));
  return herb ? herb : null;
};

export const toNumber = (keyword: NumberKeyWordType): number => {
  switch (keyword) {
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
    default:
      throw new Error(`unkown number: ${keyword}`);
  }
  return 0;
};
