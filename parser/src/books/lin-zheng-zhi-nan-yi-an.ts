import fs, { readFile } from "fs";
import os from "os";
import path from "path";
import readline from "readline";
import { promisify } from "util";
import { tryConvertToNumber } from "../prescription/utils";
import { PrescriptionItem } from "../prescription/type";
import { tryParsePrescription } from "../prescription/format2";
import { convertUom2 } from "../prescription/convertUom";

const readdir = promisify(fs.readdir);
const formattedFolder = path.join("./resource", "format", "临证指南医案");
const splitToFiles = async () => {
  const filePath = "./resource/临证指南医案.txt";
  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  let toFolderName: string = "";
  let fileNameIndex: number = 0;
  let toFilePath: string | undefined;
  let lines: string[] = [];
  for await (const rawText of readInterface) {
    if (rawText === "更多中医经典著作，请登录淘中医网（http://www.taozhy.com）") {
      continue;
    }
    if (rawText.startsWith("卷")) {
      const number = tryConvertToNumber(rawText.substring(1));
      if (number) {
        toFolderName = path.join(formattedFolder, `${number}.${rawText.trim()}`);
        fs.mkdirSync(toFolderName, { recursive: true });
        fileNameIndex = 0;
        continue;
      }
      console.error(`${rawText} is not folder name`);
    }
    if (rawText === rawText.trim() && rawText.trim()) {
      if (toFilePath) {
        fs.writeFileSync(toFilePath, lines.join(os.EOL));
      }
      fileNameIndex += 1;
      toFilePath = path.join(toFolderName, `${fileNameIndex}.${rawText}.txt`);
      lines = [];
      continue;
    }
    if (!!toFilePath) {
      lines.push(rawText);
      continue;
    }
    console.error(`${rawText} would not be written`);
  }
  if (toFilePath && lines && lines.length) {
    fs.writeFileSync(toFilePath, lines.join(os.EOL));
  }
};

const groupYiAn = async () => {
  const processFile = async (filePath: string) => {
    const readInterface = readline.createInterface({
      input: fs.createReadStream(filePath),
      output: process.stdout,
      terminal: false
    });
    const lines: string[] = [];
    for await (const rawText of readInterface) {
      let line = rawText.trim();
      if (!lines.length) {
        lines.push(line);
        continue;
      }

      if (line.startsWith("又")) {
        lines.push(line);
        continue;
      }
      //钱 偏枯在左
      //沈（四九）
      const secondChar = line.charAt(1);
      if (secondChar === " " || secondChar === "\u3000" || secondChar === "（") {
        lines.push("");
      }

      // <D>风为百病之长
      if (line.startsWith("<D>")) {
        lines.push("");
      }

      lines.push(line);
    }
    fs.writeFileSync(filePath, lines.join(os.EOL));
  };
  const processDirectory = async (directory: string) => {
    const dirents = await readdir(directory, { withFileTypes: true });
    const files = dirents.filter(x => x.isFile()).map(f => path.join(directory, f.name));
    await Promise.all(files.map(processFile));
    const dirs = dirents.filter(x => x.isDirectory()).map(x => path.join(directory, x.name));
    await Promise.all(dirs.map(processDirectory));
  };
  await processDirectory(formattedFolder);
};

type TextToken = {
  type: "text";
  value: string;
};
type PrescriptionItemToken = {
  type: "prescriptionItems";
  value: PrescriptionItem[];
};
type Token = TextToken | PrescriptionItemToken;

const createTokens = async (filePath: string): Promise<Array<Token[]>> => {
  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  const result: Array<Token[]> = [];
  let tokens: Token[] = [];
  for await (const line of readInterface) {
    if (!line) {
      if (tokens.length) {
        result.push(tokens);
      }
      tokens = [];
      continue;
    }
    const prescriptionItems = tryParsePrescription(line, convertUom2);
    if (prescriptionItems !== null) {
      tokens.push({ type: "prescriptionItems", value: prescriptionItems });
      continue;
    }
    tokens.push({ type: "text", value: line });
  }

  if (tokens.length) {
    result.push(tokens);
  }
  return result;
};

export const exportImpl = async () => {
  //await splitToFiles();
  //await groupYiAn();
  const filePath = path.join("./resource", "format", "临证指南医案", "1.卷一", "1.中风.txt");
  const result = await createTokens(filePath);
  const toFilePath = path.join("./resource", "format", "临证指南医案", "debug.txt");
  fs.writeFileSync(toFilePath, JSON.stringify(result, null, 2));
  console.log("done");
};
