import { Token, parseTokens } from "./format2";

type TokenTestData = {
  source: string;
  expectedTokens: Token[];
};

describe("format1", () => {
  it("get tokens", () => {
    //expect(true).toEqual(false);
    const testDataSet: TokenTestData[] = [
      {
        source: "生姜",
        expectedTokens: [{ type: "herb", value: "生姜" }]
      },
      {
        source: "熟地（四两）",
        expectedTokens: [
          { type: "herb", value: "熟地" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "四" },
          { type: "uom", value: "两" },
          { type: "bracketsEnd", value: "）" }
        ]
      }
    ];
    testDataSet.forEach(({ source, expectedTokens }) => {
      var actualResult = parseTokens(source);
      expect(actualResult).toEqual(expectedTokens);
    });
  });
});
