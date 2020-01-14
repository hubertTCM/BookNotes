import { Token, parseTokens, tryParsePrescription } from "./format2";
import { PrescriptionItem } from "./type";

type TokenTestData = {
  source: string;
  expectedTokens: Token[];
};
type PrescriptionItemsTestData = {
  source: string;
  expected: PrescriptionItem[] | null;
};

describe("format1", () => {
  it("get tokens", () => {
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
      },
      {
        source: "人参（一钱秋石一分化水拌烘干同煎） ",
        expectedTokens: [
          { type: "herb", value: "人参" },
          { type: "bracketsStart", value: "（" },
          { type: "number", value: "一" },
          { type: "uom", value: "钱" },
          { type: "data", value: "秋石" },
          { type: "number", value: "一" },
          { type: "uom", value: "分" },
          { type: "data", value: "化水拌烘干同煎" },
          { type: "bracketsEnd", value: "）" }
        ]
      },
      {
        source: "黄菊花膏丸",
        expectedTokens: [
          { type: "herb", value: "黄菊花" },
          { type: "data", value: "膏丸" }
        ]
      }
    ];
    testDataSet.forEach(({ source, expectedTokens }) => {
      var actualResult = parseTokens(source);
      expect(actualResult).toEqual(expectedTokens);
    });
  });

  it("text to prescription itmes", () => {
    const testDataSet: PrescriptionItemsTestData[] = [
      {
        source: "三七",
        expected: [{ herb: "三七" }]
      },
      {
        source: "燕窝菜（洗净，另熬膏，一斤）",
        expected: [{ herb: "燕窝菜", quantity: { uom: "两", value: 1 }, comment: "洗净，另熬膏，" }]
      },
      {
        source: "三角胡麻（四两，打碎，水洗十次，烘）",
        expected: [{ herb: "三角胡麻", quantity: { uom: "两", value: 4 }, comment: "，打碎，水洗十次，烘" }]
      },
      {
        source: "牛膝（四两半）",
        expected: [{ herb: "牛膝", quantity: { uom: "两", value: 4.5 } }]
      },
      {
        source: "牛膝（四两半） 三角胡麻",
        expected: [{ herb: "牛膝", quantity: { uom: "两", value: 4.5 } }, { herb: "三角胡麻" }]
      },
      {
        source: "牛膝 三角胡麻",
        expected: [{ herb: "牛膝" }, { herb: "三角胡麻" }]
      },
      {
        source: "人参（一钱秋石一分化水拌烘干同煎） ",
        expected: [{ herb: "人参", quantity: { uom: "两", value: 1 }, comment: "秋石一分化水拌烘干同煎" }]
      },
      {
        source: "黄菊花膏丸",
        expected: null
      }
    ];

    testDataSet.forEach(({ source, expected }) => {
      var actualResult = tryParsePrescription(source, quantity => {
        return { ...quantity, uom: "两" };
      });
      expect(actualResult).toEqual(expected);
    });
  });
});
