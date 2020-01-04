#!/usr/bin/env node

import { argv as args } from "yargs";
import chalk from "chalk";
import { exportShl } from "./books/shl";
import { exportHerbAlias } from "./herbAliasLoader";

const majorVersion = parseInt(process.versions.node.split(".")[0], 10);

(async () => {
  if (majorVersion < 8) {
    console.log(chalk.red.bold("Plase install node 8 or later"));
    process.exit(1);
  }
  exportHerbAlias();
  await exportShl();
  //console.log(chalk.red.bold(`${JSON.stringify(tokens).substring(0, 80)}`));
})();
