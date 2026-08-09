const TimeConstants = {
  oneMinute: 1,
  threeMinute: 2,
  fiveMinute: 3,
  fifteenMinute: 4,
  thirtyMinute: 5,
  oneHour: 6,
  fourHour: 7,
  sixHour: 8,
  oneDay: 9,
  oneWeek: 10,
  oneMonth: 11,
  minuteHour: -1,
};

const TimeTypes = {
  1: { label: "Minute", value: TimeConstants.minuteHour },
  3: { label: "3 min", value: TimeConstants.threeMinute },
  4: { label: "5 min", value: TimeConstants.fiveMinute },
  5: { label: "15 min", value: TimeConstants.fifteenMinute },
  6: { label: "30 min", value: TimeConstants.thirtyMinute },
  7: { label: "1 hour", value: TimeConstants.oneHour },
};

const DrawTypeConstants = {
  none: 0,
  show: -1,
  line: 1,
  horizontalLine: 2,
  verticalLine: 3,
  halfLine: 4,
  parallelLine: 5,
  rectangle: 101,
  parallelogram: 102,
};

const DrawStateConstants = {
  none: -3,
  showPencil: -2,
  showContext: -1,
};

const IndicatorTypes = {
  main: {
    1: { label: "MA", value: "ma" },
    2: { label: "BOLL", value: "boll" },
    0: { label: "NONE", value: "none" },
  },
  sub: {
    3: { label: "MACD", value: "macd" },
    4: { label: "KDJ", value: "kdj" },
    5: { label: "RSI", value: "rsi" },
    6: { label: "WR", value: "wr" },
    0: { label: "NONE", value: "none" },
  },
};

const indicatorSelecters = {
  isMASelected: ({ selectedMainIndicator }) => selectedMainIndicator === 1,
  isBOLLSelected: ({ selectedMainIndicator }) => selectedMainIndicator === 2,
  isMACDSelected: ({ selectedSubIndicator }) => selectedSubIndicator === 3,
  isKDJSelected: ({ selectedSubIndicator }) => selectedSubIndicator === 4,
  isRSISelected: ({ selectedSubIndicator }) => selectedSubIndicator === 5,
  isWRSelected: ({ selectedSubIndicator }) => selectedSubIndicator === 6,
};

const DrawToolTypes = {
  [DrawTypeConstants.none]: {
    label: "Close Drawing",
    value: DrawTypeConstants.none,
  },
  [DrawTypeConstants.line]: { label: "Line", value: DrawTypeConstants.line },
  [DrawTypeConstants.horizontalLine]: {
    label: "Horizontal Line",
    value: DrawTypeConstants.horizontalLine,
  },
  [DrawTypeConstants.verticalLine]: {
    label: "Vertical Line",
    value: DrawTypeConstants.verticalLine,
  },
  [DrawTypeConstants.halfLine]: {
    label: "Ray",
    value: DrawTypeConstants.halfLine,
  },
  [DrawTypeConstants.parallelLine]: {
    label: "Parallel Channel",
    value: DrawTypeConstants.parallelLine,
  },
  [DrawTypeConstants.rectangle]: {
    label: "Rectangle",
    value: DrawTypeConstants.rectangle,
  },
  [DrawTypeConstants.parallelogram]: {
    label: "Parallelogram",
    value: DrawTypeConstants.parallelogram,
  },
};

const getTargetList = (selectedIndicators) => {
  return {
    maList: [
      {
        title: "5",
        selected: indicatorSelecters.isMASelected(selectedIndicators),
        index: 0,
      },
      {
        title: "10",
        selected: indicatorSelecters.isMASelected(selectedIndicators),
        index: 1,
      },
      {
        title: "20",
        selected: indicatorSelecters.isMASelected(selectedIndicators),
        index: 2,
      },
    ],
    maVolumeList: [],
    bollN: "20",
    bollP: "2",
    macdS: "12",
    macdL: "26",
    macdM: "9",
    kdjN: "9",
    kdjM1: "3",
    kdjM2: "3",
    rsiList: [
      {
        title: "6",
        selected: indicatorSelecters.isRSISelected(selectedIndicators),
        index: 0,
      },
      {
        title: "12",
        selected: indicatorSelecters.isRSISelected(selectedIndicators),
        index: 1,
      },
      {
        title: "24",
        selected: indicatorSelecters.isRSISelected(selectedIndicators),
        index: 2,
      },
    ],
    wrList: [
      {
        title: "14",
        selected: indicatorSelecters.isWRSelected(selectedIndicators),
        index: 0,
      },
    ],
  };
};

const normalizeKLineModelArray = (modelArray, getDateString) => {
  return modelArray.map((item) => {
    const sourceTimestamp =
      typeof item?.time === "number"
        ? item.time
        : typeof item?.id === "number"
          ? item.id * 1000
          : Date.now();

    const id =
      typeof item?.id === "number"
        ? item.id
        : Math.floor(sourceTimestamp / 1000);

    return {
      ...item,
      id,
      vol: typeof item?.vol === "number" ? item.vol : (item?.volume ?? 0),
      dateString:
        typeof item?.dateString === "string" && item.dateString.length > 0
          ? item.dateString
          : getDateString(sourceTimestamp),
      selectedItemList: Array.isArray(item?.selectedItemList)
        ? item.selectedItemList
        : [],
    };
  });
};

export {
  DrawStateConstants,
  DrawToolTypes,
  DrawTypeConstants,
  IndicatorTypes,
  TimeConstants,
  TimeTypes,
  getTargetList,
  indicatorSelecters,
  normalizeKLineModelArray,
};
