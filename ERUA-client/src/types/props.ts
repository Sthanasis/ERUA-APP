export type TInputControl = {
  name: string;
  type:
    | 'text'
    | 'password'
    | 'select'
    | 'textarea'
    | 'checkbox'
    | 'date'
    | 'file';
  label?: string;
  required?: boolean;
};

type SnackbarVariantType = 'error' | 'success' | 'info';
export interface ISnackbar {
  text: string;
  timeout?: number;
  variant: SnackbarVariantType;
}

export interface IPiniaSnackbar extends ISnackbar {
  id: number;
}
