import { Quantity, PrescriptionItem } from "./type";
import { tryParsePrescription as tryParsePrescriptionFromat1 } from "./format1";
import { tryParsePrescription as tryParsePrescriptionFromat2 } from "./format2";

type Format = "format1" | "format2";

export const tryParsePrescription = (
  text: string,
  convertUOM: (quanity: Quantity) => Quantity
): PrescriptionItem[] | null => {
  const formats: Format[] = ["format1"];
  let result: PrescriptionItem[] | null = null;
  for (const format of formats) {
    switch (format) {
      case "format1":
        result = tryParsePrescriptionFromat1(text, convertUOM);
        break;
      case "format2":
        result = tryParsePrescriptionFromat2(text, convertUOM);
        break;
      default:
        result = null;
    }
    if (result !== null) {
      return result;
    }
  }
  return null;
};
