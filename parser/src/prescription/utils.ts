import herbInfo from "../../src/herbs.json";

export const findHerb = (text: string, start: number = 0): string | null => {
  if (!text || text.length <= start) {
    return null;
  }
  const source = start > 0 ? text.substring(start) : text;
  const herb = herbInfo.allHerbNames.find(x => source.startsWith(x));
  return herb ? herb : null;
};
