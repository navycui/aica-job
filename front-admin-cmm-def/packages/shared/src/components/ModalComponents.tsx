import React, {CSSProperties, useEffect} from 'react';
import {Button, Modal, Stack} from '@mui/material';
import Box from '@mui/material/Box';
import {styled} from '@mui/styles';
import {VerticalInterval} from '../components/LayoutComponents';
import {Icons} from '../components/IconContainer';
import {CustomButton} from '../components/ButtonComponents';

export type ModalType = 'normal' | 'confirm' | 'save' | 'transmit' | 'preview' | 'submit';

export type ModalParam = {
  open: boolean;
  content?: string;
  type?: string;
  hideBackdrop?: boolean;
  title?: string;
  isDist?: boolean;
  confirmLabel?: string
  cancelLabel?: string
  outlined?: boolean
  onConfirm?: () => void;
  onClose?: () => void;
  style?: CSSProperties;
}

export const ModalComponents: React.FC<ModalParam> = (props) => {
    const {type, isDist} = props;
    let confirmLabel = ""
    if (type == "normal") {
      confirmLabel = "확인";
    } else if (type == "save") {
      confirmLabel = "저장";
    } else if (type == "transmit") {
      confirmLabel = "전송";
    } else if (type == 'preview') {
      confirmLabel = '닫기'
    } else if (type == 'submit') {
      confirmLabel = '전송'
    } else {
      confirmLabel = "예";
    }

    const handlerConfirm = (event: React.MouseEvent<HTMLElement>) => {
      if (props.onConfirm) props.onConfirm();
        document.body.style.overflow = 'unset';
    };

    const handlerExit = (event: React.MouseEvent<HTMLElement>) => {
      if (props.onClose) props.onClose();
        document.body.style.overflow = 'unset';
    };

    const handlerModalClose = (
      event: {},
      reason: 'backdropClick' | 'escapeKeyDown'
    ) => {
      if (props.onClose) props.onClose();
    };

    useEffect(() => {
      if (props.open) {
        document.body.style.overflow = 'hidden';
      } else {
        document.body.style.overflow = 'unset';
      }
    }, [props.open])

    return (
      <>
        <Modal
          disableScrollLock
          open={props.open}
          onClose={handlerModalClose}
          hideBackdrop={props.hideBackdrop}
        >
          <ModalBoxContainer>
            <Stack justifyContent={'space-between'} flexDirection={'column'}>
              <Box style={{display: 'flex', justifyContent: 'right'}}>
                <Button
                  style={{width: '24px', height: '24px'}}
                  onClick={handlerExit}
                >
                  {<Icons.Exit/>}
                </Button>
              </Box>

              <Box sx={{pb: '32px'}}>
                {props.children ? (
                  <>
                    {props.title ? (
                      <h2>{props.title}</h2>
                    ) : (
                      <VerticalInterval size={'40px'}/>
                    )}
                    {props.children}
                  </>
                ) : (
                  <>
                    {props.title ? (
                      <h2 style={{textAlign: 'center'}}>{props.title}</h2>
                    ) : (
                      <VerticalInterval size={'40px'}/>
                    )}
                    <p style={{textAlign: 'center', whiteSpace: 'pre-wrap'}}>{props.content}</p>
                  </>
                )}
              </Box>

              <Stack justifyContent={type === 'preview' ? 'space-between' : 'center'} direction={'row'} spacing={'10px'}>
                {isDist || (
                  <CustomButton
                    type={'large'}
                    label={props.cancelLabel || '취소'}
                    color={props.outlined ? 'outlined' : 'primary'}
                    onClick={handlerExit}
                  />
                )}
                {
                  type === 'preview' && (
                    <CustomButton
                      type={'large'} color={'outlined'}
                      label={'오늘은 그만보기'}
                      onClick={handlerExit}
                    />
                  )}
                <CustomButton label={confirmLabel} onClick={handlerConfirm}/>
              </Stack>
            </Stack>
          </ModalBoxContainer>
        </Modal>
      </>
    );
  }
;

const ModalBoxContainer = styled(Box)({
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  minWidth: '480px',
  backgroundColor: 'white',
  borderRadius: '20px',
  margin: '18px 220px 18px 0',
  padding: '20px 30px 20px 30px',
})
