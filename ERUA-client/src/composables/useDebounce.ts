export const useDebounce = (
  // eslint-disable-next-line no-unused-vars
  cb: (value: string) => Promise<void> | void,
  delay = 500
) => {
  let timeoutId: number;

  function debounceFunction(value: string) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => {
      cb(value);
    }, delay) as unknown as number;
  }

  return { debounceFunction };
};
