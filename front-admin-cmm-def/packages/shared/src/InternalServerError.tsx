type Props = {
  [key: string]: any;
};

function InternalServerError({ error }: Props) {
  return (
    <section>
      <div>문제가 발생하여 페이지 열 수 없습니다.</div>
    </section>
  );
}

export default InternalServerError;
