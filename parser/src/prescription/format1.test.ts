import { Token, parseTokens, token2PrescriptionItems, tryParsePrescription } from "./format1";
import { PrescriptionItem } from "./type";
import { convertUom1 } from "./convertUom";

type TestData = {
  source: string;
  expectedTokens: Token[];
};

type TestTokens = {
  source: Token[];
  expected: PrescriptionItem[];
};

type TestPrescriptionItems = {
  source: string;
  expected: PrescriptionItem[];
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

  it("tokens to prescription itmes", () => {
    const testDataSet: TestTokens[] = [
      {
        source: [
          { type: "herb", value: "三七" },
          { type: "number", value: "三" },
          { type: "uom", value: "两" },
          { type: "number", value: "五" },
          { type: "uom", value: "钱" },
          { type: "herb", value: "生姜" },
          { type: "number", value: "七" },
          { type: "uom", value: "两" }
        ],
        expected: [
          { herb: "三七", quantity: { uom: "两", value: 8 } },
          { herb: "生姜", quantity: { uom: "两", value: 7 } }
        ]
      }
    ];
    testDataSet.forEach(({ source, expected }) => {
      var actualResult = token2PrescriptionItems(source, quantity => {
        return { ...quantity, uom: "两" };
      });
      expect(actualResult).toEqual(expected);
    });
  });

  it("text to prescription itmes", () => {
    const testDataSet: TestPrescriptionItems[] = [
      {
        source: "三七三两 生姜七两",
        expected: [
          { herb: "三七", quantity: { uom: "两", value: 3 } },
          { herb: "生姜", quantity: { uom: "两", value: 7 } }
        ]
      },
      {
        source: "乌梅三百枚",
        expected: [{ herb: "乌梅", quantity: { uom: "枚", value: 300 } }]
      },
      {
        source: "麻黄六两 石膏如鸡子大（碎）",
        expected: [
          { herb: "麻黄", quantity: { uom: "两", value: 6 } },
          { herb: "石膏", quantity: { uom: "鸡子大", value: 1 }, comment: "碎" }
        ]
      },
      {
        source: "半夏（洗，破如枣核）十四枚　鸡子一枚（去黄，内上苦酒，着鸡子壳中）",
        expected: [
          { herb: "半夏", quantity: { uom: "枚", value: 14 }, comment: "洗，破如枣核" },
          { herb: "鸡子", quantity: { uom: "枚", value: 1 }, comment: "去黄，内上苦酒，着鸡子壳中" }
        ]
      },
      {
        source: "大黄六两 甘遂一钱匕",
        expected: [
          { herb: "大黄", quantity: { uom: "两", value: 6 } },
          { herb: "甘遂", quantity: { uom: "钱匕", value: 1 } }
        ]
      },
      {
        source: "代赭石一两",
        expected: [{ herb: "代赭石", quantity: { uom: "两", value: 1 } }]
      },
      { source: "半夏十四枚", expected: [{ herb: "半夏", quantity: { uom: "枚", value: 14 } }] },
      { source: "半夏三四枚", expected: [{ herb: "半夏", quantity: { uom: "枚", value: 34 } }] },
      {
        source: "牡蛎（熬）　泽泻　蜀漆（暖水洗去腥）　葶苈子（熬）　商陆根（熬）　海藻（洗去咸）　栝蒌根各等分",
        expected: [
          { herb: "牡蛎", comment: "熬" },
          { herb: "泽泻" },
          { herb: "蜀漆", comment: "暖水洗去腥" },
          { herb: "葶苈子", comment: "熬" },
          { herb: "商陆根", comment: "熬" },
          { herb: "海藻", comment: "洗去咸" },
          { herb: "栝蒌根" }
        ]
      }
    ];

    testDataSet.forEach(({ source, expected }) => {
      var actualResult = tryParsePrescription(source, convertUom1);
      expect(actualResult).toEqual(expected);
    });
  });
});
