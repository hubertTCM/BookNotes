module.exports = {
  presets: [["@babel/env", { modules: "commonjs" }], "@babel/typescript"],
  plugins: [
    "@babel/proposal-class-properties",
    "@babel/proposal-object-rest-spread"
  ]
};
