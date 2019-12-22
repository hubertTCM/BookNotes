import { Token, parseTokens } from "./format1";

type TestData = {
  source: string;
  expectedTokens: Token[];
};
describe("format1", () => {
  it("get tokens", () => {
    //expect(true).toEqual(false);
    const testDataSet: TestData[] = [
      {
        source: "生姜",
        expectedTokens: [{ type: "herb", value: "生姜" }]
      },
      {
        source: "生姜 三七各三两",
        expectedTokens: [
          { type: "herb", value: "生姜" },
          { type: "herb", value: "三七" },
          { type: "applyQuantityToPrevious", value: "各" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" }
        ]
      },
      {
        source: "生姜三两",
        expectedTokens: [
          { type: "herb", value: "生姜" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" }
        ]
      },
      {
        source: "生姜三两（切）",
        expectedTokens: [
          { type: "herb", value: "生姜" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" },
          { type: "comment", value: "切" }
        ]
      },
      {
        source: "生姜（切）",
        expectedTokens: [
          { type: "herb", value: "生姜" },
          { type: "comment", value: "切" }
        ]
      },
      {
        source: "三七三两五钱",
        expectedTokens: [
          { type: "herb", value: "三七" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" },
          { type: "number", value: "五" },
          { type: "uom", value: "钱" }
        ]
      },
      {
        source: "三七三两五钱 生姜七两",
        expectedTokens: [
          { type: "herb", value: "三七" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" },
          { type: "number", value: "五" },
          { type: "uom", value: "钱" },
          { type: "herb", value: "生姜" },
          { type: "number", value: "七" },
          { type: "uom", value: "两" }
        ]
      }
    ];
    testDataSet.forEach(({ source, expectedTokens }) => {
      var actualResult = parseTokens(source);
      expect(actualResult).toEqual(expectedTokens);
    });
  });
});
