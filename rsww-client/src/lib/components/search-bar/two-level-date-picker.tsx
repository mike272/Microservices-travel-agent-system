import { DatePicker } from "antd";

type Props = {
  label: string;
  placeholder?: string;
  value?: string;
  onChange: (date: any, dateString: any) => void;
};
export const TwoLevelDatePicker = (props: Props) => {
  const { label, placeholder, value, onChange } = props;
  const format = "DD/MM/YY";
  return (
    <div className="flex flex-col">
      <label>{label}</label>
      <DatePicker format={format} onChange={onChange} />
    </div>
  );
};
