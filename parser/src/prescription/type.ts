export const uomKeyWords = ["两", "钱", "铢", "个", "枚"] as const;
export type UOMKeyWordType = typeof uomKeyWords[number];

export const numberKeyWords = [
  "一",
  "二",
  "三",
  "四",
  "五",
  "六",
  "七",
  "八",
  "九"
] as const;
export type NumberKeyWordType = typeof numberKeyWords[number];

export type Quantity = {
  value: number;
  uom: UOMKeyWordType;
};
export type Composition = {
  herb: string;
  quantity?: Quantity;
  comment?: string;
};
export type Prescription = {
  name?: string;
  compositions: Composition[];
  comment?: string;
};
