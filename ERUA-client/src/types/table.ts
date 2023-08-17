export type TAction = { btnLabel: string; handler: () => void };

export type TRow = {
  [key: string]: string | number | TAction | undefined | boolean;
  id: string;
  action?: TAction;
};

export type TColumn = {
  [key: string]: string | boolean | undefined;
  title: string;
  key: string;
};
