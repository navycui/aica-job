import React from 'react';
import { createPortal } from 'react-dom';

type Props = {
  selector?: string;
  children?: React.ReactNode;
  [key: string]: any;
};

function Portal({ selector = 'body', children, ...args }: Props) {
  const domNode = document.querySelector(selector);
  return createPortal(children, domNode!);
}

export default Portal;
