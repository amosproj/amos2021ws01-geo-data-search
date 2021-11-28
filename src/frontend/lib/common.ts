
/**
 * Merge CSS classNames
 */
export function cc(classNames: (string | boolean | undefined)[]) {
  return classNames.filter((e) => !!e).join(" ");
}
