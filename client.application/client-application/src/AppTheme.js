import { createTheme, ThemeProvider } from "@mui/material";


export default function AppTheme({ children }) {
    const theme = createTheme({ cssVariables: true });

    return <ThemeProvider theme={theme}>
        {children}
    </ThemeProvider>;
}