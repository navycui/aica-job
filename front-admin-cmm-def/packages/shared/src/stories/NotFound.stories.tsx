import { ComponentStory, ComponentMeta } from '@storybook/react';

import Component from '../NotFound';

export default {
  title: 'Error',
  component: Component,
  argTypes: {},
} as ComponentMeta<typeof Component>;

export const NotFound: ComponentStory<typeof Component> = () => <Component />;
