export function compareLocation(
  location1: string,
  location2?: string,
  location3?: string
): boolean {
  return (
    (location2 && location1.toLowerCase() === location2.toLowerCase()) ||
    (location3 && location1.toLowerCase() === location3.toLowerCase())
  );
}
export function uid(): number {
  return Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
}
