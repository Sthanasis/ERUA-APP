export type PageAction = {
  condition?: boolean;
  callback: () => void;
  color?: string;
  name?: string;
  icon?: string;
};
