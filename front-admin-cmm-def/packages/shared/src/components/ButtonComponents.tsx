import React, {CSSProperties, MouseEventHandler, useEffect, useLayoutEffect, useState} from 'react';
import {
  Button,
  Checkbox,
  CircularProgress,
  FormControlLabel,
  FormGroup,
  FormLabel,
  IconButton,
  Radio,
  RadioGroup
} from "@mui/material";
import FormControl from "@mui/material/FormControl";
import {Color} from "../components/StyleUtils";
import styled from '@emotion/styled';
import {Icons} from '../components/IconContainer';
import async from "async";

export interface DefaultCheckBoxProps {
  label: string;
  disabled?: boolean;
  checked?: boolean;
}


const ButtonStyles = {
  //버튼의 크기관련 요소
  large: {
    minWidth: '200px',
    height: '56px',
    br: 0,
  },
  largeList: {
    minWidth: '100px',
    height: '56px',
    br: 0,
  },
  small: {
    minWidth: '65px',
    height: '40px',
    br: 5,
  },
};
const ButtonColorStyles = {
  //버튼의 색상관련 요소
  primary: {
    fg: Color.white,
    bg: Color.primary,
    border: 'none',
  },
  secondary: {
    fg: Color.white,
    bg: Color.secondary,
    border: 'none',
  },
  outlined: {
    border: '1px solid' + Color.primary,
    fg: Color.primary,
    bg: Color.white,
  },
  outlined_del: {
    border: '1px solid' + Color.pinkish_grey,
    fg: Color.textDefault,
    bg: Color.white,
  },
  list: {
    fg: Color.primary,
    bg: Color.list,
    border: 'none',
  },
  item: {
    fg: Color.brownishGrey,
    bg: Color.light_gray,
    border: 'none',
  },
};

export const CustomButton: React.FC<{
  label: string;
  type?: keyof typeof ButtonStyles;
  color?: keyof typeof ButtonColorStyles;
  onClick?: (event: React.MouseEvent<HTMLElement>) => void;
  disabled?: boolean;
  style?: React.CSSProperties;
}> = (props) => {
  const buttonStyle = props.type
    ? ButtonStyles[props.type]
    : ButtonStyles.large;
  const buttonColorStyle = props.color
    ? ButtonColorStyles[props.color]
    : ButtonColorStyles.primary;
  return (
    <Button
      onClick={props.onClick}

      style={{
        minWidth: buttonStyle.minWidth,
        height: buttonStyle.height,
        backgroundColor: props.disabled
          ? Color.gray_sub_button
          : buttonColorStyle.bg,
        color: buttonColorStyle.fg,
        borderRadius: buttonStyle.br,
        border: buttonColorStyle.border,
        padding: '0 15px',
        fontWeight: 'bold',
        fontSize: '16px',
        ...props.style,
      }}
      disabled={props.disabled}
    >
      {props.label}
    </Button>
  );
};

export const CustomLoadingButton: React.FC<{
  label: string;
  type?: keyof typeof ButtonStyles;
  color?: keyof typeof ButtonColorStyles;
  loadingSize?: number
  loadingColor?: string
  onClick?: (event: React.MouseEvent<HTMLElement>) => Promise<any>;
  disabled?: boolean;
  style?: React.CSSProperties;
}> = (props) => {
  const [loading, setLoading] = useState(false)
  const buttonStyle = props.type
    ? ButtonStyles[props.type]
    : ButtonStyles.large;
  const buttonColorStyle = props.color
    ? ButtonColorStyles[props.color]
    : ButtonColorStyles.primary;
  return (
    <Button
      onClick={async (e) => {
        try {
          if (props.onClick) {
            setLoading(true)
            await props.onClick(e)
          }
        }catch (e){
          setLoading(false)
        }
        setLoading(false)
      }}

      style={{
        minWidth: buttonStyle.minWidth,
        height: buttonStyle.height,
        backgroundColor: props.disabled
          ? Color.gray_sub_button
          : buttonColorStyle.bg,
        color: buttonColorStyle.fg,
        borderRadius: buttonStyle.br,
        border: buttonColorStyle.border,
        padding: '0 15px',
        fontWeight: 'bold',
        fontSize: '16px',
        ...props.style,
      }}
      disabled={props.disabled}
      endIcon={loading && <CircularProgress
        size={props.loadingSize || 25}
        sx={{color: props.loadingColor || buttonColorStyle.fg}}
      />}
    >
      {props.label}
    </Button>
  );
};

export const CustomRadioButtons: React.FC<{
  data: string[]
  defaultData?: string
  row?: boolean
  justifyContent?: 'center' | 'right' | 'left'
  style?: CSSProperties
  onClick?: (selected: string) => void
}> = (props) => {
  let initIndex = 0;
  if (props.defaultData)
    initIndex = props.data.findIndex(i => props.defaultData == i)
  const [value, setValue] = useState(props.data[initIndex > 0 ? initIndex : 0]);

  useEffect(() => {
    if (!!props.defaultData) {
      setValue(props.defaultData)
      if (props.onClick) props.onClick(props.defaultData)
    }
  }, [props.defaultData])

  const handlerChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValue(event.target.value);
    if (props.onClick) props.onClick(event.target.value);
  };

  return (
    <FormControl>
      <RadioGroup row={props.row} value={value} onChange={handlerChange}>
        {props.data.map((m, i) => {
          return (
            <FormControlLabel
              key={i}
              value={m}
              control={<RadioStyle/>}
              label={m}
            />
          );
        })}
      </RadioGroup>
    </FormControl>
  );
};

export const CustomCheckBoxs: React.FC<{
  checkbox: string[]
  defaultSelected?: string[]
  isAll?: boolean
  row?: boolean
  justifyContent?: 'center' | 'right' | 'left'
  children?: React.ReactNode
  style?: CSSProperties
  onClick?: (selected: string[]) => void;
}> = (props) => {
  const [selected, setSelected] = useState<string[]>(props.defaultSelected ? props.defaultSelected : []);

  useEffect(() => {
    if (props.defaultSelected) setSelected(props.defaultSelected)
  }, [props.defaultSelected])

  useEffect(() => {
    if (props.onClick) props.onClick(selected);
  }, [selected]);

  useEffect(() => {
    if (props.isAll != undefined) {
      if (props.isAll) {
        setSelected(props.checkbox)
      } else if (!props.isAll && selected.length == props.checkbox.length) {
        setSelected([])
      }
    }
  }, [props.isAll])

  const handlerCheck = (label: string) => {
    const selectedIndex = selected.indexOf(label);
    let newSelected: string[] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, label);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1)
      );
    }
    setSelected(newSelected);
  };

  return (
    <FormControl>
      <FormGroup
        row={props.row}
        style={{
          justifyContent: props.justifyContent,
          ...props.style,
        }}
      >
        {
          props.checkbox.map((m, i) => {
            return (
              <FormControlLabel
                key={i}
                control={
                  <CheckboxStyle
                    checked={selected.includes(m)}
                    onClick={(e: React.MouseEvent<HTMLButtonElement>) => {
                      handlerCheck(m);
                    }}
                  />
                }
                label={m}
              />
            );
          })}
        {props.children}
      </FormGroup>
    </FormControl>
  );
};

export const CustomIconButton: React.FC<{
  icon: () => React.ReactNode;
  startText?: string;
  endText?: string;
  style?: React.CSSProperties;
  onClick?: () => void
}> = props => {
  return <IconButtonStyle
    onClick={props.onClick}
    style={props.style}
  >
    {props.startText}
    {props.icon()}
    {props.endText}
  </IconButtonStyle>
}

const IconButtonStyle = styled(IconButton)`
  border-radius: 10px;
`;

const RadioStyle = styled(Radio)`
  .MuiSvgIcon-root {
    width: 20px;
    height: 20px;
    border: 1px solid #d8dbe7;
    border-radius: 100%;
    background-color: #fff;

    path {
      display: none;
    }
  }

  &.Mui-checked {
    &:before {
      content: '';
      position: absolute;
      top: 50%;
      left: 50%;
      width: 10px;
      height: 10px;
      margin: -5px 0 0 -5px;
      border-radius: 100%;
      background-color: #4063ec;
      z-index: 10;
    }

    .MuiSvgIcon-root {
      position: relative;
      border-color: #4063ec;

      &[data-testid='RadioButtonCheckedIcon'] {
        display: none;
      }
    }
  }
`;

export const CheckboxStyle = styled(Checkbox)`
  .MuiSvgIcon-root {
    width: 20px;
    height: 20px;
    background-color: #fff;
    border-radius: 3px;

    path {
      display: none;
    }
  }

  &:before {
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    width: 20px;
    height: 20px;
    margin: -10px 0 0 -10px;
    border: 1px solid #d8dbe7;
    border-radius: 3px;
  }

  &:after {
    content: '';
    position: absolute;
    left: 50%;
    top: 50%;
    width: 10px;
    height: 6px;
    margin: -4px 0 0 -5px;
    border-left: 2px solid #d8dbe7;
    border-bottom: 2px solid #d8dbe7;
    transform: rotate(-45deg);
  }

  &.Mui-checked {
    &:before {
      border-color: #4063ec;
      background-color: #4063ec;
    }

    &:after {
      border-color: #fff;
    }
  }
`;
