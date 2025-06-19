export function expirationTime(minutes: number): number {
  const now = Date.now();
  return now + minutes * 60 * 1000;
}

export function isExpired(expirationTime: number): boolean {
  const now = Date.now();
  return now > expirationTime;
}
