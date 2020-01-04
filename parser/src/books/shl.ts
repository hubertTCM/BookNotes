//伤寒论

import fs from "fs";
import readline from "readline";
import { PrescriptionItem } from "../prescription/type";
import { tryParsePrescription } from "../prescription/format1";
import { convertUom1 } from "../prescription/convertUom";
import { toNumber } from "../prescription/utils";
import { Stack } from "../stack";

export type Prescription =
  | {
      name: string;
      items: PrescriptionItem[];
      comment?: string;
    }
  | PrescriptionWithoutAccurateItems;

/*
烧裈散方
妇人中裈近隐处，取烧作灰。
上一味，水服方寸匕。日三服。小便即利，阴头微肿，此为愈矣。妇人病，取男子裈烧服。
*/
export type PrescriptionWithoutAccurateItems = {
  name: string;
  description: string;
  comment?: string;
};

export type TiaoWen = {
  order: number;
  text: string;
  prescriptions: Prescription[];
  comment: string;
};

type TiaoWenTextToken = {
  type: "tiaowenText";
  value: string;
  order: number;
};
type TextToken = {
  type: "data" | "presctionName" | "tiaowenComment" | "prescriptionInaccurateText";
  value: string;
};
type PrescriptionItemToken = {
  type: "prescriptionItems";
  value: PrescriptionItem[];
};

type Token = TextToken | TiaoWenTextToken | PrescriptionItemToken;

type ASTTiaowenText = {
  type: "tiaowenText";
  value: string;
  order: number;
};

type ASTText = {
  type: "prescriptionName" | "tiaowenComment";
  value: string;
};

type ASTPrescription = {
  type: "prescription";
  value: Prescription;
};

type ASTNode = ASTTiaowenText | ASTText | ASTPrescription;

// regex: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions
const tryParseTiaoWenText = (line: string): TiaoWenTextToken | undefined => {
  var splitIndex = line.indexOf("、");
  if (splitIndex < 0) {
    return undefined;
  }
  try {
    const order = toNumber(line.substring(0, splitIndex));
    return { type: "tiaowenText", value: line, order };
  } catch (e) {
    return undefined;
  }
};

const isTiaowenComment = (text: string) => {
  return text.startsWith("臣（林）亿等谨按：") || text.startsWith("附子泻心汤，本云：");
};

const tryParsePrescriptionName = (text: string): string | undefined => {
  // 猪胆汁方（附方）
  const endTags = ["方（附方）", "方"];
  const endTag = endTags.find(tag => {
    if (text.endsWith(tag)) {
      return tag;
    }
  });
  if (endTag) {
    return text.substring(0, text.length - endTag.length);
  }
  return undefined;
};

const createTokens = async (): Promise<Token[]> => {
  const filePath = "./resource/伤寒论.txt";

  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  const tokens: Token[] = [];
  for await (const rawText of readInterface) {
    const line = rawText.trim();
    if (!line) {
      continue;
    }
    const tiaowenTextToken = tryParseTiaoWenText(line);
    if (tiaowenTextToken !== undefined) {
      tokens.push(tiaowenTextToken);
      continue;
    }
    const prescriptionName = tryParsePrescriptionName(line);
    if (prescriptionName) {
      tokens.push({ type: "presctionName", value: prescriptionName });
      continue;
    }
    const prescriptionItems = tryParsePrescription(rawText, convertUom1);
    if (prescriptionItems !== null) {
      tokens.push({ type: "prescriptionItems", value: prescriptionItems });
      continue;
    }

    if (isTiaowenComment(line)) {
      tokens.push({ type: "tiaowenComment", value: line });
      continue;
    }

    if (line === "土瓜根方（附方佚）") {
      continue;
    }

    if (line === "妇人中裈近隐处，取烧作灰。") {
      tokens.push({ type: "prescriptionInaccurateText", value: line });
      continue;
    }

    tokens.push({ type: "data", value: line });
  }
  return tokens;
};

const createTiaowen = (astStack: Stack<ASTNode>): TiaoWen | undefined => {
  if (astStack.count() === 0) {
    return undefined;
  }

  let node = astStack.pop();
  let order: number = 0;
  let tiaowenText: string = "";
  let tiaowenComment: string = "";
  const prescriptions: Prescription[] = [];
  while (node) {
    switch (node.type) {
      case "tiaowenText":
        if (astStack.count() > 0) {
          throw new Error("");
        }
        order = node.order;
        tiaowenText = node.value;
        break;
      case "prescription":
        prescriptions.push(node.value);
        break;
      case "tiaowenComment":
        tiaowenComment = node.value;
        break;
      default:
        throw new Error(`unexpected node. "${JSON.stringify(node)}"`);
    }
    node = astStack.pop();
  }
  return { order, text: tiaowenText, prescriptions, comment: tiaowenComment };
};

const token2Tiaowen = (tokens: Token[]): TiaoWen[] => {
  const tiaowen: TiaoWen[] = [];
  const astStack = new Stack<ASTNode>();
  for (let i = 0; i < tokens.length; i++) {
    const token = tokens[i];
    switch (token.type) {
      case "tiaowenText":
        const currentTiaowen = createTiaowen(astStack);
        if (currentTiaowen !== undefined) {
          tiaowen.push(currentTiaowen);
        }
        astStack.push({ ...token });
        break;
      case "presctionName":
        astStack.push({ type: "prescriptionName", value: token.value });
        break;
      case "prescriptionInaccurateText":
        const temp = astStack.pop();
        if (temp === undefined) {
          throw new Error(`no name for prescription: ${JSON.stringify(token)}`);
        }
        if (temp.type !== "prescriptionName") {
          throw new Error(
            `no name for prescription. prescription "${JSON.stringify(token)}" node:"${JSON.stringify(temp)}"`
          );
        }
        astStack.push({
          type: "prescription",
          value: {
            name: temp.value,
            description: token.value
          }
        });
        break;
      case "prescriptionItems":
        const nameNode = astStack.pop();
        if (nameNode === undefined) {
          throw new Error(`no name for prescription: ${JSON.stringify(token)}`);
        }
        if (nameNode.type !== "prescriptionName") {
          throw new Error(
            `no name for prescription. prescription "${JSON.stringify(token)}" node:"${JSON.stringify(nameNode)}"`
          );
        }
        astStack.push({
          type: "prescription",
          value: {
            name: nameNode.value,
            items: token.value
          }
        });
        break;
      case "data":
        const prescriptionNode = astStack.top();
        if (prescriptionNode === undefined) {
          throw new Error(`no prescription for: "${JSON.stringify(token)}"`);
        }
        if (prescriptionNode.type !== "prescription") {
          throw new Error(
            `expect prescription for comment: "${JSON.stringify(token)}", actual is ${JSON.stringify(prescriptionNode)}`
          );
        }
        const existingComment = prescriptionNode.value.comment;
        if (existingComment) {
          throw new Error(
            `comment already there. current comment "${existingComment}" token:"${JSON.stringify(
              token
            )}" prescriptionNode "${JSON.stringify(prescriptionNode)}"`
          );
        }
        prescriptionNode.value.comment = token.value;
        break;
      case "tiaowenComment":
        astStack.push({ type: "tiaowenComment", value: token.value });
        break;
      default:
        throw new Error(`unknown token "${JSON.stringify(token)}"`);
    }
  }
  const lastTiaowen = createTiaowen(astStack);
  if (lastTiaowen !== undefined) {
    tiaowen.push(lastTiaowen);
  }
  return tiaowen;
};

export const exportShl = async (): Promise<TiaoWen[]> => {
  const tokens = await createTokens();
  const tiaowen = token2Tiaowen(tokens);
  fs.writeFileSync("resource/伤寒论.json", `${JSON.stringify(tiaowen, null, 2)}`);
  return tiaowen;
};
