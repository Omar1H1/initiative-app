const DateService = (dateString: string) => {
  const messageDate = new Date(dateString);
  const now = new Date();
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
  const yesterday = new Date(today);
  yesterday.setDate(today.getDate() - 1);

  const isToday = messageDate.toDateString() === today.toDateString();
  const isYesterday = messageDate.toDateString() === yesterday.toDateString();

  if (isToday) {
    return `Aujourd'hui, ${messageDate.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
      timeZone: "Europe/Paris",
    })}`;
  } else if (isYesterday) {
    return "hier";
  } else {
    return messageDate.toLocaleString([], {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
      timeZone: "Europe/Paris",
    });
  }
};

export default DateService;
