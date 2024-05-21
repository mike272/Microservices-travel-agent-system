function formatDate(year: number, month: number, day: number) {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
}

const monthNames = [
  "Jan",
  "Feb",
  "Mar",
  "Apr",
  "May",
  "Jun",
  "Jul",
  "Aug",
  "Sep",
  "Oct",
  "Nov",
  "Dec",
];

function getDateNDaysFromToday(n: number) {
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + n);

  const year = tomorrow.getFullYear();
  const month = String(tomorrow.getMonth() + 1).padStart(2, "0"); // Months are 0-based in JS
  const day = String(tomorrow.getDate()).padStart(2, "0");

  const formattedDate = `${year}-${month}-${day}`;
  return formattedDate;
}

function formatTwoDatesToString(dateFrom: string, dateTo: string) {
  const dateFromArray = dateFrom.split("-");
  const dateToArray = dateTo.split("-");
  // if year is 2024, we don't need to display it
  let dateToFormatted, dateFromFormatted;
  if (dateFromArray[0] === "2024" && dateToArray[0] === "2024") {
    dateFromFormatted = `${dateFromArray[2]} ${
      monthNames[parseInt(dateFromArray[1]) - 1]
    }`;
    dateToFormatted = `${dateToArray[2]} ${
      monthNames[parseInt(dateToArray[1]) - 1]
    }`;
  } else {
    dateFromFormatted = `${dateFromArray[2]} ${
      monthNames[parseInt(dateFromArray[1]) - 1]
    } ${dateFromArray[0]}`;
    dateToFormatted = `${dateToArray[2]} ${
      monthNames[parseInt(dateToArray[1]) - 1]
    } ${dateToArray[0]}`;
  }

  return `${dateFromFormatted} - ${dateToFormatted}`;
}

export { formatDate, getDateNDaysFromToday, formatTwoDatesToString };
