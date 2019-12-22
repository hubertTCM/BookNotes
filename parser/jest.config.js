module.exports = {
  rootDir: ".",
  transform: {
    "^.+\\.[jt]sx?$": "ts-jest"
  },
  testMatch: ["**/?(*.)test.ts?(x)"],
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json", "node"],
  collectCoverage: true,
  coverageReporters: ["lcov"],
  //setupFilesAfterEnv: ["./src/setupTests.ts"],
  reporters: ["default", "jest-junit"]
};
