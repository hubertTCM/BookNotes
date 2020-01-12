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
      },
      {
        source: "生姜 熟地（四两）",
        expectedTokens: [
          { type: "herb", value: "生姜" },
          { type: "herb", value: "熟地" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "四" },
          { type: "uom", value: "两" },
          { type: "bracketsEnd", value: "）" }
        ]
      },
      {
        source: "牛膝（四两半）",
        expectedTokens: [
          { type: "herb", value: "牛膝" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "四" },
          { type: "uom", value: "两" },
          { type: "number", value: "半" },
          { type: "bracketsEnd", value: "）" }
        ]
      },
      {
        source: "制首乌（四两，烘） ",
        expectedTokens: [
          { type: "herb", value: "制首乌" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "四" },
          { type: "uom", value: "两" },
          { type: "data", value: "，烘" },
          { type: "bracketsEnd", value: "）" }
        ]
      },
      {
        source: "三角胡麻（四两，打碎，水洗十次，烘）",
        expectedTokens: [
          { type: "herb", value: "三角胡麻" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "四" },
          { type: "uom", value: "两" },
          { type: "data", value: "，打碎，水洗" },
          { type: "number", value: "十" },
          { type: "data", value: "次，烘" },
          { type: "bracketsEnd", value: "）" }
        ]
      },
      {
        source: "燕窝菜（洗净，另熬膏，一斤）",
        expectedTokens: [
          { type: "herb", value: "燕窝菜" },
          { type: "bracketsStart", value: "（" },
          { type: "data", value: "洗净，另熬膏，" },
          { type: "number", value: "一" },
          { type: "uom", value: "斤" },
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
