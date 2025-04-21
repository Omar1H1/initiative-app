const DateService = (dateString: string) => {
  const messageDate = new Date(dateString);
  const now = new Date();
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const yesterday = new Date(today);
  yesterday.setDate(today.getDate() - 1);

  const isToday = messageDate.toDateString() === today.toDateString();
  const isYesterday = messageDate.toDateString() === yesterday.toDateString();

  if (isToday) {
    return `Aujourd'hui, ${messageDate.toUTCString().split(" ")[4]}`;
  } else if (isYesterday) {
    return "hier";
  } else {
    return messageDate.toUTCString();
  }
};

export default DateService;
