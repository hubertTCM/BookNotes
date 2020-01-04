import { toNumber, formatNumberText } from "./utils";
describe("utils", () => {
  it("formatNumberText", () => {
    const testDataSet = [
      { source: "三百", expected: "三零零" },
      { source: "五百三十四", expected: "五三四" },
      { source: "十四", expected: "一四" },
      { source: "百三十四", expected: "一三四" },
      //{ source: "百十四", expected: "一一四" },
      { source: "三四", expected: "三四" },
      { source: "百三四", expected: "一三四" }
    ];
    testDataSet.forEach(({ source, expected }) => {
      var actualResult = formatNumberText(source);
      expect(actualResult).toEqual(expected);
    });
  });
  it("toNumber", () => {
    const testDataSet = [
      { source: "三百", expected: 300 },
      { source: "五百三十四", expected: 534 },
      { source: "十四", expected: 14 },
      { source: "百三十四", expected: 134 },
      //{ source: "百十四", expected: 114 }, // TODO:
      { source: "三四", expected: 34 },
      { source: "百三四", expected: 134 }
    ];
    testDataSet.forEach(({ source, expected }) => {
      var actualResult = toNumber(source);
      expect(actualResult).toEqual(expected);
    });
  });
});
