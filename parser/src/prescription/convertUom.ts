import { Quantity } from "./type";

/* 
http://www.cntcm.com.cn/xueshu/2016-05/06/content_14730.htm
“古秤唯有铢两，而无分名。今则以十黍为一铢，六铢为一分，四分成一两，十六两为一斤。
虽有子谷黍之制，从来均之已久，正尔依此用之。但古秤皆复，今南秤是也。
晋秤始后，汉末以来，分一斤为二斤耳，一两为二两耳。金银丝绵，并与药同，无轻重矣。
古方唯有仲景，而已涉今秤，若用古秤作汤，则水为殊少，故知非复秤，悉用今者尔。”

单位换算: https://www.haodf.com/zhuanjiaguandian/luoluludr_6474775810.htm
1 石 = 四钧 
1 钧 = 三十斤
1 斤 = 16 两
1 两 = 24 铢

1 圭 = 0.5 克
1 撮 = 2 克
1 方寸匕 = 金石类2.74 克 = 药末约2 克 = 草木类药末约1 克
半方寸匕 = 一刀圭 = 一钱匕 = 1.5 克
一分 = 3.9-4.2 克
1 斛 = 10 斗 = 20000 毫升
1 斗 = 10 升 = 2000 毫升
1 升 = 10 合 = 200 毫升
1 合 = 2 龠 = 20 毫升
1 龠 = 5 撮 = 10 毫升
1 撮 = 4 圭 = 2 毫升
1 圭 = 0.5 毫升
1 引 = 10 丈 = 2310 厘米
1 丈 = 10 尺 = 231 厘米
1 尺 = 10 寸 = 23.1 厘米
1 寸 = 10 分 = 2.31 厘米
1 分 = 0.231 厘米
梧桐子大 = 黄豆大
蜀椒一升 = 50 克
葶苈子一升 = 60 克
吴茱萸一升 = 50 克
五味子一升 = 50 克
半夏一升 = 130 克
虻虫一升 = 16 克
附子大者1 枚 = 20-30 克
附子中者1 枚 = 15 克
强乌头1 枚小者 = 3 克
强乌头1 枚大者 = 5-6 克
杏仁大者10 枚 = 4 克
栀子10 枚 平均15 克
瓜蒌大小平均1 枚 = 46 克
枳实1 枚 约14.4 克
石膏鸡蛋大1 枚 约40 克
厚朴1 尺 约30 克
竹叶一握 约12 克
*/
export const convertUom1 = (quantity: Quantity): Quantity => {
  switch (quantity.uom) {
    case "斤":
      return { value: quantity.value * 16, uom: "两" };
    case "两":
      return { ...quantity };
    // 麻黄升麻汤：
    // 麻黄二两半（去节）　升麻一两一分　当归一两一分 ...
    case "分":
      return { value: quantity.value / 4.0, uom: "两" };
    case "铢":
      const value = quantity.value / 24.0;
      return { value, uom: "两" };
    default:
      return { ...quantity };
  }
};

export const convertUom2 = (quantity: Quantity): Quantity => {
  switch (quantity.uom) {
    case "斤":
      return { value: quantity.value * 16, uom: "两" };
    case "两":
      return { ...quantity };
    case "分":
      return { value: quantity.value / 10.0, uom: "两" };
    default:
      return { ...quantity };
  }
};
