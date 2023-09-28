import { ComponentStory, ComponentMeta } from '@storybook/react';

import { Button as Component } from '@mui/material';

export default {
  title: 'MUI Customize',
  component: Component,
  argTypes: {},
} as ComponentMeta<typeof Component>;

export const Button: ComponentStory<typeof Component> = (args) => (
  <Component {...args}>TEST</Component>
);
Button.args = {
  variant: 'test',
};
