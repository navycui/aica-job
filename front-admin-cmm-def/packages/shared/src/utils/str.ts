/**
 * 3자리 마다 (,)콤마
 * @example
 * str.currency('1234' || 1234); => '1,234'
 */

export const currency = (str: string | number) => {
  const value = str.toString();
  return value.length
    ? parseInt(value.replace(/([^0-9\-])|(.-)/g, ''))
        .toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    : '0';
};

export const random = () => {
  return Math.random().toString(36).slice(2);
};

export default { currency, random };
