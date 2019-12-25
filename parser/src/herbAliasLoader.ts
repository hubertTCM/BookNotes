import fs from "fs";
import readline from "readline";

export type HerbAlias = {
  name: string;
  alias: string[];
};

export const parseAliasFromSingleLine = (line: string): HerbAlias | null => {
  // 土三七：  藤三七，为落葵科植物落葵薯的块茎。处方别名：土三七 藤三七
  // 混淆品：关白附：为毛茛科植物黄花乌头的块根。处方别名：关白附、竹节白附
  const cleanValue = line && line.trim();
  if (!cleanValue) {
    return null;
  }
  const ignorePrefix = "混淆品：";
  if (cleanValue.startsWith(ignorePrefix)) {
    return parseAliasFromSingleLine(cleanValue.substring(ignorePrefix.length));
  }
  const nameIndex = cleanValue.indexOf("：");
  if (nameIndex <= 0) {
    console.log(`$$$ no herb in line: ${line}`);
    return null;
  }

  // remove white space
  const name = cleanValue.substring(0, nameIndex).replace(/\s/g, "");

  const aliasPrefix = "处方别名：";
  const aliasIndex = cleanValue.indexOf(aliasPrefix);
  if (aliasIndex <= 0) {
    return {
      name,
      alias: []
    };
  }
  const otherPart = cleanValue.substring(aliasIndex + aliasPrefix.length);
  const alias = otherPart.split("、").reduce((previous, item) => {
    let temp = item.trim();
    // 文山七（产云南文山）
    const bracketIndex = temp.indexOf("（");
    if (bracketIndex > 0) {
      temp = temp.substring(0, bracketIndex);
    }

    // "土三七：  藤三七，为落葵科植物落葵薯的块茎。处方别名：土三七 藤三七";
    const shouldBeRemoved = [` ${name}`, `${name} `];
    shouldBeRemoved.forEach(x => {
      if (temp.includes(x)) {
        temp = temp.replace(x, "");
      }
    });
    temp = temp.replace(/\s/g, "");
    return temp && temp !== name ? [...previous, temp] : previous;
  }, [] as string[]);
  return { name, alias };
};

export const createHerbAlias = async (filePath: string): Promise<HerbAlias[]> => {
  let result: HerbAlias[] = [];
  const readInterface = readline.createInterface({
    input: fs.createReadStream(filePath),
    output: process.stdout,
    terminal: false
  });

  let herbSection = false;
  for await (const line of readInterface) {
    if (line.startsWith("==== 处方药：")) {
      herbSection = true;
      continue;
    }
    if (herbSection) {
      const herbs = line.trim().split(" ");
      herbs.forEach(herb => {
        result.push({ name: herb, alias: [] });
      });
    }
    if (!herbSection) {
      const temp = parseAliasFromSingleLine(line);
      temp && result.push(temp);
    }
  }

  return result.sort((x, y) => x.name.length - y.name.length);
};

export const exportHerbAlias = async () => {
  const aliasResult = await createHerbAlias("./resource/常用中药处方别名.txt");
  const herbs = {
    allHerbNames: aliasResult
      .reduce((previousResult, item) => {
        return [...previousResult, item.name].concat(item.alias);
      }, [] as string[])
      .sort((x, y) => y.length - x.length),
    alias: aliasResult
  };
  fs.writeFileSync("src/herbs.json", `${JSON.stringify(herbs, null, 2)}`);
};
