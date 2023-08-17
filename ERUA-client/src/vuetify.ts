import 'vuetify/styles';
import '@mdi/font/css/materialdesignicons.css';
import { createVuetify, ThemeDefinition } from 'vuetify';
import { aliases, mdi } from 'vuetify/iconsets/mdi';

const lightTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#f0eeeec7',
    surface: '#FFFFFF',
    primary: '#0C00FF',
    'primary-darken-1': '#3700B3',
    secondary: '#47F89A',
    'secondary-darken-1': '#018786',
    error: '#B00020',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FB8C00',
    grey: '#83878C',
  },
};

const vuetify = createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: 'lightTheme',
    themes: {
      lightTheme,
    },
  },
});

export default vuetify;
