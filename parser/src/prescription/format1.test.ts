import { Token, parseTokens, token2PrescriptionItems, tryParsePrescription } from "./format1";
import { PrescriptionItem } from "./type";

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
