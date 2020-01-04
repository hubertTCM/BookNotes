import { Configuration, NamedModulesPlugin } from "webpack";
import ForkTsCheckerWebpackPlugin from "fork-ts-checker-webpack-plugin";
import path from "path";
const config: Configuration = {
  resolve: {
    extensions: [".ts", ".js"]
  },
  entry: {
    index: "./src/cli/index.ts"
  },
  mode: "development",
  output: {
    filename: "index.js",
    path: path.resolve(__dirname, "./bin/")
    //path: "../src/main/resources/static"
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader"
        }
      },
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        loader: "babel-loader"
      }
    ]
  },

  plugins: [
    new NamedModulesPlugin(),
    new ForkTsCheckerWebpackPlugin({
      workers: ForkTsCheckerWebpackPlugin.ONE_CPU,
      memoryLimit: 512
    })
  ]
};

export default config;
