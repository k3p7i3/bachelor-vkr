
import { Grid2, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography, Paper } from "@mui/material"
import TariffConditionCards from "./TariffConditionCards" 
import { ConditionHeaderTableCell, ConditionTableCell } from "./TariffConditionTableCell"
import { TariffPriceCell } from "./TariffPriceCell"

function TariffPricesTable({tariff, tariffTable}) {
  return <TableContainer component={Paper} variant="outlined">
    <Table>
      {(tariffTable.columns.length > 0 || tariffTable.rows.length > 0) &&
        <TableHead>
          
          <TableRow>
            {tariffTable.columns.length > 0 && tariffTable.rows.length > 0 &&
              <TableCell
                sx={{
                  backgroundImage: 'linear-gradient(to bottom left, transparent calc(50% - 1px), var(--mui-palette-grey-200), transparent calc(50% + 1px))'
                }}>
                <Grid2 container>
                  <Grid2 size={6}/>
                  <Grid2 size={6}>{tariffTable.columns &&
                      <ConditionHeaderTableCell tariff={tariff} condition={tariffTable.columns[0]}/>}</Grid2>
                  <Grid2 size={6}>{tariffTable.rows && 
                      <ConditionHeaderTableCell tariff={tariff} condition={tariffTable.rows[0]}/>}</Grid2>
                  <Grid2 size={6}/>
                </Grid2>
              </TableCell>
            } 

            {tariffTable.columns.length > 0 && tariffTable.columns.map((column) => {
              return <TableCell>
                <ConditionTableCell tariff={tariff} condition={column}/>
              </TableCell>
            })}

          </TableRow>
      </TableHead>  
      }

      <TableBody>
        {tariffTable.rows.length > 0 ?
          tariffTable.rows.map((row, index) => {
            return <TableRow>
              <TableCell>
                <ConditionTableCell tariff={tariff} condition={row}/>
              </TableCell>

              {tariffTable.tariffPrices[index].map((tablePrice) => {
                return <TableCell>
                  <TariffPriceCell price={tablePrice}/>
                </TableCell>
              })}
              
            </TableRow>
          })
          : (
            tariffTable.columns.length > 0 ? 
              <TableRow>
                  {tariffTable.tariffPrices[0].map((tablePrice) => {
                    return <TableCell>
                      <TariffPriceCell price={tablePrice}/>
                    </TableCell>
                  })}
              </TableRow>
              : <TableCell><TariffPriceCell price={tariffTable.tariffPrices[0][0]}/></TableCell>
          )
        }
      </TableBody>

    </Table>
  </TableContainer>
}

export default function TariffTable({tariff, tariffTable}) {
  return <Stack spacing={2}> 
    <Typography variant='h6'>Условия:</Typography>
    <TariffConditionCards tariff={tariff} conditions={tariffTable.commonConditions}/>
    <TariffPricesTable tariff={tariff} tariffTable={tariffTable}/>
  </Stack>
}