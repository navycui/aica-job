import { ComponentStory, ComponentMeta } from '@storybook/react';

import Component from '../InternalServerError';

export default {
  title: 'Error',
  component: Component,
  argTypes: {},
} as ComponentMeta<typeof Component>;

export const InternalServerError: ComponentStory<typeof Component> = (args) => (
  <Component {...args} />
);
