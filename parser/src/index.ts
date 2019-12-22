#!/usr/bin/env node

import fs from "fs";
import { argv as args } from "yargs";
import chalk from "chalk";
import { createHerbAlias } from "./herbAliasLoader";
import { parseTokens } from "./prescription/format1";

const majorVersion = parseInt(process.versions.node.split(".")[0], 10);

(async () => {
  if (majorVersion < 8) {
    console.log(chalk.red.bold("Plase install node 8 or later"));
    process.exit(1);
  }
  const tokens = parseTokens(
    "生姜三两（切）　大枣十二枚（擘）生姜一两六铢（切）杏仁十六个（去皮尖）白术　茯苓各三两"
  );
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
  console.log(chalk.red.bold(`${JSON.stringify(tokens).substring(0, 80)}`));
})();
