export const uomKeyWords = [
  "鸡子大",
  "钱匕",
  "斤",
  "两",
  "钱",
  "分",
  "铢",
  "个",
  "枚",
  "升",
  "把",
  "合",
  "尺",
  "茎"
] as const;
export type UOMKeyWordType = typeof uomKeyWords[number];

export const numberKeyWords = ["零", "半", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "百"] as const;
export type NumberKeyWordType = typeof numberKeyWords[number];

export type QuantityToken = {
  type: "number" | "uom";
  value: string;
};

export type Quantity = {
  value: number;
  uom: UOMKeyWordType;
};
export type PrescriptionItem = {
  herb: string;
  quantity?: Quantity;
  comment?: string;
};
