import fs from "fs";
import os from "os";
import path from "path";
import readline from "readline";
import herbInfo from "./herbs.json";
const filePath = path.join("./resource", "herbs.txt");
const toFilePath = path.join("./resource", "missing-herb.txt");

export const findMissingHerbs = async () => {
  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  const missingHerbs: string[] = [];
  for await (const rawText of readInterface) {
    const line = rawText.trim();
    const herbs = line.split(" ");
    for (const x of herbs) {
      const index = x.indexOf("（");
      const herb = index < 0 ? x : x.substring(0, index);
      if (missingHerbs.includes(herb)) {
        continue;
      }
      if (herbInfo.allHerbNames.includes(herb)) {
        continue;
      }
      missingHerbs.push(herb);
    }
  }
  fs.writeFileSync(toFilePath, missingHerbs.join(`${os.EOL}、`));
};
