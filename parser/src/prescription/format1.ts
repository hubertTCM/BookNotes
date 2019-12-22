// 生姜三两（切）　大枣十二枚（擘）生姜一两六铢（切）杏仁十六个（去皮尖）白术　茯苓各三两
// 生姜 大枣
import {
  numberKeyWords,
  NumberKeyWordType,
  uomKeyWords,
  UOMKeyWordType
} from "./type";
import { findHerb } from "./utils";

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
        throw new Error(
          `unexpected token ${char} currentTokenType:${currentTokenType}`
        );
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
