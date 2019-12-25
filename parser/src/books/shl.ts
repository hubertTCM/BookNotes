//伤寒论

import fs from "fs";
import readline from "readline";
import { PrescriptionItem } from "../prescription/type";
import { tryParsePrescription } from "../prescription/format1";
import { convertUom1 } from "../prescription/convertUom";
import { toNumber } from "../prescription/utils";

type Prescription = {
  name: string;
  items: PrescriptionItem[];
  comment?: string;
};

type TiaoWen = {
  order: number;
  text: string;
  prescription?: Prescription;
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

const token2Tiaowen = (tokens: Token[]): TiaoWen[] => {
  const tiaowen: TiaoWen[] = [];
  let currentTiaowen: TiaoWen | undefined = undefined;
  let currentPrescriptionName: string = "";
  let currentPrescriptionItmes: PrescriptionItem[] | undefined = undefined;
  let currentComment: string | undefined = undefined;
  tokens.forEach(token => {
    switch (token.type) {
      case "tiaowenText":
        if (currentTiaowen !== undefined) {
          tiaowen.push(currentTiaowen);
        }
        currentTiaowen = {
          text: token.value,
          order: token.order
        };
        if (currentPrescriptionName === undefined && currentPrescriptionItmes !== undefined) {
          throw new Error(`failed to parse tiaowen ${JSON.stringify(currentTiaowen)}`);
        }
        currentTiaowen.prescription = {
          name: currentPrescriptionName || "",
          items: currentPrescriptionItmes || [],
          comment: currentComment
        };
        currentPrescriptionName = "";
        currentPrescriptionItmes = undefined;
        currentComment = undefined;
        break;
      case "presctionName":
        currentPrescriptionName = token.value;
        break;
      case "prescriptionItems":
        currentPrescriptionItmes = token.value;
        break;
      case "data":
        if (currentComment) {
          throw new Error(
            `comment already there. current comment "${currentComment}" token:"${JSON.stringify(
              token
            )}" currentTiaowen "${JSON.stringify(currentTiaowen)}"`
          );
        }
        currentComment = token.value;
        break;
    }
  });
  if (currentTiaowen !== undefined) {
    tiaowen.push(currentTiaowen);
  }
  return tiaowen;
};

export const exportShl = async (): Promise<TiaoWen[]> => {
  const tokens = await parse();
  const tiaowen = token2Tiaowen(tokens);
  console.log(JSON.stringify(tiaowen));
  return tiaowen;
};
