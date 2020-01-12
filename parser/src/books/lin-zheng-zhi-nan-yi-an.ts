import fs from "fs";
import os from "os";
import path from "path";
import readline from "readline";
import { tryConvertToNumber } from "../prescription/utils";

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

// TODO:
const groupYiAn = () => {};

export const exportImpl = async () => {
  await splitToFiles();
};
