//伤寒论

import fs from "fs";
import readline from "readline";
import { PrescriptionItem } from "../prescription/type";
import { tryParsePrescription } from "../prescription/format1";
import { convertUom1 } from "../prescription/convertUom";
import { toNumber } from "../prescription/utils";
import { Stack } from "../stack";

type Prescription = {
  name: string;
  items: PrescriptionItem[];
  comment?: string;
};

type TiaoWen = {
  order: number;
  text: string;
  prescriptions: Prescription[];
};

type TiaoWenTextToken = {
  type: "tiaowenText";
  value: string;
  order: number;
};
type TextToken = {
  type: "data" | "presctionName";
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

type ASTPrescriptionName = {
  type: "prescriptionName";
  value: string;
};

type ASTPrescription = {
  type: "prescription";
  value: Prescription;
};

type ASTNode = ASTTiaowenText | ASTPrescriptionName | ASTPrescription;

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

export const parse = async (): Promise<Token[]> => {
  const filePath = "./resource/伤寒论.txt";

  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  const tokens: Token[] = [];
  for await (const rawText of readInterface) {
    const line = rawText.trim();
    console.log(rawText);
    if (!line) {
      continue;
    }
    const tiaowenTextToken = tryParseTiaoWenText(line);
    if (tiaowenTextToken !== undefined) {
      tokens.push(tiaowenTextToken);
      continue;
    }
    if (line.endsWith("方")) {
      const name = line.substring(0, line.length - 1);
      tokens.push({ type: "presctionName", value: name });
      continue;
    }
    const prescriptionItems = tryParsePrescription(rawText, convertUom1);
    if (prescriptionItems !== null) {
      tokens.push({ type: "prescriptionItems", value: prescriptionItems });
      continue;
    }

    tokens.push({ type: "data", value: line });
  }
  return tokens;
};

const createTiaowen = (astStack: Stack<ASTNode>): TiaoWen | undefined => {
  let node = astStack.pop();
  const prescriptions: Prescription[] = [];
  while (node) {
    switch (node.type) {
      case "tiaowenText":
        if (astStack.count() > 0) {
          throw new Error("");
        }
        return { order: node.order, text: node.value, prescriptions };
      case "prescription":
        prescriptions.push(node.value);
        break;
      default:
        throw new Error(`unexpected node. "${JSON.stringify(node)}"`);
    }
    node = astStack.pop();
  }

  return undefined;
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
        const prescriptionNode = astStack.pop();
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
    }
  }
  const lastTiaowen = createTiaowen(astStack);
  if (lastTiaowen !== undefined) {
    tiaowen.push(lastTiaowen);
  }
  return tiaowen;
};

export const exportShl = async (): Promise<TiaoWen[]> => {
  const tokens = await parse();
  const tiaowen = token2Tiaowen(tokens);
  console.log(JSON.stringify(tiaowen));
  return tiaowen;
};
