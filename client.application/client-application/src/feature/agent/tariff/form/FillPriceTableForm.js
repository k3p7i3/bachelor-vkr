import { useState, useRef } from "react";
import { Stack, Table, TableHead, TableCell, TableBody, TableRow, 
  TableFooter, Button, Typography, Grid2, IconButton } from "@mui/material";
import ClearIcon from '@mui/icons-material/Clear';

import { features } from "./tariffTableFormMapUtils";
import FillTableNumericCondition from "./FillTableNumericCondition";
import { mapNumericType } from "../TariffMapUtils";
import FillTablePricePerUnit from "./FillTablePricePerUnit";

export default function FillPriceTableForm({ columnsGeneralCondition, rowsGeneralCondition, generalPricePerUnit, setStep }) {

  const isRowsNumeric = rowsGeneralCondition.type == "NUMERIC";
  const isColumnsNumeric = columnsGeneralCondition.type == "NUMERIC";

  const rowIdCnt = useRef(0);
  const colIdCnt = useRef(0);

  const makeEnumHeaders = (featureId, idCnt) => {
    const feature = features.find((f) => (f.featureId === featureId))
    return feature?.options
      ?.map((option) => ({
        id: idCnt.current++,
        feature: {
          featureId: featureId,
          title: feature.title 
        },
        option: {
          optionId: option.optionId,
          title: option.title
        }
      }))
  }

  const [rows, setRows] = useState(
    isRowsNumeric ? [] : makeEnumHeaders(rowsGeneralCondition.typeCondition.feature, rowIdCnt)
  );
  const [columns, setColumns] = useState(
    isColumnsNumeric ? [] : makeEnumHeaders(columnsGeneralCondition.typeCondition.feature, colIdCnt)
  );

  const [prices, setPrices] = useState(
    Array(
      isRowsNumeric ? 0 
      : (features.find((f) => (f.featureId === rowsGeneralCondition.typeCondition.feature))?.options?.length || 0)
    ).fill().map(() => 
      Array(
        isColumnsNumeric ? 0 
        : (features.find((f) => (f.featureId === columnsGeneralCondition.typeCondition.feature))?.options?.length || 0)
      ).fill().map(() => (
        {
          price: {
            value: '',
            unit: generalPricePerUnit.price.unit
          },
          perUnit: {
            unitType: generalPricePerUnit.perUnit.unitType,
            unit: generalPricePerUnit.perUnit.unit
          }
        }
      ))
    )
  );

  const addNewNumericRow = (e) => {
    const newRow = {
      id: rowIdCnt.current++,
      minLimit: '',
      maxLimit: '',
      measurementType: rowsGeneralCondition.numericCondition.measurementType,
      measurementUnit: rowsGeneralCondition.numericCondition.measurementUnit
    };
    setRows([...rows, newRow]);
    
    setPrices([
      ...prices, 
      Array(columns.length).fill().map(() => (
        {
          price: {
            value: '',
            unit: generalPricePerUnit.price.unit
          },
          perUnit: {
            unitType: generalPricePerUnit.perUnit.unitType,
            unit: generalPricePerUnit.perUnit.unit
          }
        }
      ))
    ]);
  };

  const deleteNumericRow = (rowId) => {
    const rowIndex = rows.findIndex((row) => (row.id == rowId));

    setRows(rows.filter((row) => (row.id != rowId)));
    setPrices(prices.filter((p, index) =>(index != rowIndex)));
  };

  const addNewNumericColumn = (e) => {
    const newColumn = {
      id: colIdCnt.current++,
      minLimit: '',
      maxLimit: '',
      measurementType: columnsGeneralCondition.numericCondition.measurementType,
      measurementUnit: columnsGeneralCondition.numericCondition.measurementUnit
    }
    setColumns([...columns, newColumn]);

    setPrices(
      prices.map((priceRow) => ([
        ...priceRow, 
        {
          price: {
            value: '',
            unit: generalPricePerUnit.price.unit
          },
          perUnit: {
            unitType: generalPricePerUnit.perUnit.unitType,
            unit: generalPricePerUnit.perUnit.unit
          }
        }
      ]))
    );
  };

  const deleteNumericColumn = (columnId) => {
    const colIndex = columns.findIndex((col) => (col.id == columnId));

    setColumns(columns.filter((col) => (col.id != columnId)));
    setPrices(
      prices.map((priceRow) => (
        priceRow.filter((p, index) => (index != colIndex))
      ))
    );
  };

  return <Stack>
    <Table>
      {(columnsGeneralCondition || rowsGeneralCondition) &&
        <TableHead>
          <TableRow>

            {columnsGeneralCondition && rowsGeneralCondition &&
              <TableCell
                sx={{backgroundImage: 'linear-gradient(to bottom left, transparent calc(50% - 1px), var(--mui-palette-grey-200), transparent calc(50% + 1px))' }}>
                <Grid2 container>
                  <Grid2 size={6}/>
                  <Grid2 size={6}>{columnsGeneralCondition  &&
                      <Typography>
                        {isColumnsNumeric 
                        ? mapNumericType[columnsGeneralCondition.numericCondition.measurementType] 
                        : features.find((f) => (f.featureId === columnsGeneralCondition.typeCondition.feature)).title}
                      </Typography>}
                  </Grid2>
                  <Grid2 size={6}>{rowsGeneralCondition && 
                      <Typography>
                        {isRowsNumeric 
                        ? mapNumericType[rowsGeneralCondition.numericCondition.measurementType] 
                        : features.find((f) => (f.featureId === rowsGeneralCondition.typeCondition.feature)).title
                        }
                      </Typography>}
                  </Grid2>
                  <Grid2 size={6}/>
                </Grid2>
              </TableCell>
            } 
            
            {columns && columns.map((column) => {
              return <TableCell key={"col_" + column.id}>
                <Stack direction="row" alignItems="center">
                  {
                    isColumnsNumeric 
                    ? <FillTableNumericCondition condition={column}/> 
                    : <Typography>{column.option.title}</Typography>
                  }
                  {
                    isColumnsNumeric &&
                      <IconButton onClick={() => deleteNumericColumn(column.id)}><ClearIcon fontSize="small"/></IconButton>
                  }
                  
                </Stack> 
              </TableCell>
            })}

            {isColumnsNumeric &&
              <TableCell>
                <Button onClick={addNewNumericColumn}>+</Button>
              </TableCell>
            }
          </TableRow>
        </TableHead>
      }

      <TableBody>
      {rowsGeneralCondition &&
        rows.map((row, index) => {
          return <TableRow key={"row_" + row.id}>
            <TableCell>
              <Stack direction="row" alignItems="center">
                {isRowsNumeric 
                  ? <FillTableNumericCondition condition={row}/> 
                  : <Typography>{row.option.title}</Typography>
                }
                {isRowsNumeric && 
                  <IconButton onClick={() => deleteNumericRow(row.id)}><ClearIcon fontSize="small"/></IconButton>
                }
                
              </Stack>
            </TableCell>

            {prices[index].map((price, colIndex) => (
              <TableCell key={"row_" + row.id + "_col_" + columns[colIndex].id}>
                <FillTablePricePerUnit price={price}/>
              </TableCell>
            ))}
          </TableRow>
        })
      }
      </TableBody>
      

    </Table>

    {isRowsNumeric && 
      <Button sx={{alignSelf: "flex-start"}} onClick={addNewNumericRow}>+ добавить строку</Button>
    }

    <Button onClick={() => {setStep(0)}}>Назад</Button>
  </Stack>
}