import { parseSingleLine, HerbAlias } from "./herbAliasLoader";

type TestData = {
  source: string;
  expectedResult: HerbAlias;
};
describe("parseSingleLine", () => {
  it("alias not null", () => {
    const testDataSet: TestData[] = [
      {
        source:
          "土三七：  藤三七，为落葵科植物落葵薯的块茎。处方别名：土三七 藤三七",
        expectedResult: {
          name: "土三七",
          alias: ["藤三七"]
        }
      },
      {
        source:
          "混淆品：关白附：为毛茛科植物黄花乌头的块根。处方别名：关白附、竹节白附",
        expectedResult: {
          name: "关白附",
          alias: ["竹节白附"]
        }
      },
      {
        source:
          "三 七：为五加科植物三七的根。商品规格：主根分60头、80头、120头、无数头。茎基称：剪口；支根称：筋条；更细的根称：绒根。 处方别名：三七、田七、参三七、汉三七、旱三七、田三七、田漆、滇七、山漆、金不换、文山七（产云南文山）",
        expectedResult: {
          name: "三七",
          alias: [
            "田七",
            "参三七",
            "汉三七",
            "旱三七",
            "田三七",
            "田漆",
            "滇七",
            "山漆",
            "金不换",
            "文山七"
          ]
        }
      },
      {
        source: "混淆品：丁公藤：为旋花科植物丁公藤的根、茎。（有毒植物）",
        expectedResult: {
          name: "丁公藤",
          alias: []
        }
      }
    ];
    testDataSet.forEach(({ source, expectedResult }) => {
      var actualResult = parseSingleLine(source);
      expect(actualResult?.name).toEqual(expectedResult.name);
      expect(actualResult?.alias).toEqual(expectedResult.alias);
    });
  });

  it("alias is null", () => {
    const testDataSet: string[] = ["常用中药处方别名"];
    testDataSet.forEach(line => {
      var actualResult = parseSingleLine(line);
      expect(actualResult).toBeNull();
    });
  });
});
