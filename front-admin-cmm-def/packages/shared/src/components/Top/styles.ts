import { css } from '@emotion/react';

export const container = css`
  position: fixed;
  bottom: 8px;
  right: 8px;
  transition: opacity 0.3s ease-out;
  display: flex;
  align-items: center;
  justify-content: center;
  &.enter,
  &.exit-active {
    opacity: 0;
  }

  &.enter-active,
  &.enter-done {
    opacity: 1;
  }
`;
