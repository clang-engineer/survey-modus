enum type {
  TEXT = 'TEXT',
  RADIO = 'RADIO',
  SELECT_BOX = 'SELECT_BOX',
  CHECK_BOX = 'CHECK_BOX',
  DATE = 'DATE',
  TIME = 'TIME',
  DATETIME = 'DATETIME',
  BOOLEAN = 'BOOLEAN',
  INTEGER = 'INTEGER',
  FLOAT = 'FLOAT',
}

const isLookupType = (data: type): boolean => {
  return [type.RADIO, type.SELECT_BOX, type.CHECK_BOX].includes(data);
};

export { isLookupType };
export default type;
